# SpringBoot 整合ElasticSearch

## 一、准备数据

### 版本

ElasticSearch 版本：7.6.2

Kibana 版本：7.6.2



### 数据

准备了一份顾客银行账户信息的虚构的 JSON 文档样本。每个文档都有下列的schema（模式）。

```json
{
	"account_number": 1,
	"balance": 39225,
	"firstname": "Amber",
	"lastname": "Duke",
	"age": 32,
	"gender": "M",
	"address": "880 Holmes Lane",
	"employer": "Pyrami",
	"email": "amberduke@pyrami.com",
	"city": "Brogan",
	"state": "IL"
}
```

https://raw.githubusercontent.com/elastic/elasticsearch/master/docs/src/test/resources/accounts.json 导入测试数据。



在 Kibana 的 Dev Tools 中执行：

```
POST bank/_bulk
{"index":{"_id":"1"}}
{"account_number":1,"balance":39225,"firstname":"Amber","lastname":"Duke","age":32,"gender":"M","address":"880 Holmes Lane","employer":"Pyrami","email":"amberduke@pyrami.com","city":"Brogan","state":"IL"}
{"index":{"_id":"6"}}
{"account_number":6,"balance":5686,"firstname":"Hattie","lastname":"Bond","age":36,"gender":"M","address":"671 Bristol Street","employer":"Netagy","email":"hattiebond@netagy.com","city":"Dante","state":"TN"}
{"index":{"_id":"13"}}
...(省略)
```



## 二、整合

使用 elasticsearch-rest-high-level-client 客户端。

官方文档：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-getting-started-initialization.html

> 文档中有引入依赖信息，API 调用信息等。

- 导入依赖

```xml
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>7.4.2</version>
</dependency>
```



- 配置类

```java
@Configuration
public class ElasticsearchConfig {

    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization", "Bearer " + TOKEN);
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    }
}
```



- 测试数据

测试客户端是否注入成功

```java
@Test
public void contextLoads() {
  System.out.println(restHighLevelClient);
}
```



测试添加或更新数据

```java
/**
* 测试存储数据，更新数据
* @throws IOException
*/
@Test
public void indexData() throws IOException {
  IndexRequest indexRequest = new IndexRequest("users");
  indexRequest.id("1");
  User user = new User();
  user.setUserName("Jack");
  user.setAge(22);
  user.setGender("男");
  String jsonString = JSON.toJSONString(user);
  // 设置要保存的内容
  indexRequest.source(jsonString, XContentType.JSON);
  // 执行创建索引和保存数据
  IndexResponse index = restHighLevelClient.index(indexRequest, ElasticsearchConfig.COMMON_OPTIONS);
  System.out.println(index);
}
```



测试检索数据（聚合）：

```java
/**
* 复杂检索:在bank中搜索address中包含mill的所有人的年龄分布以及平均年龄，平均薪资
* @throws IOException
*/
@Test
public void searchData() throws IOException {
  //1. 创建检索请求
  SearchRequest searchRequest = new SearchRequest();

  //1.1）指定索引
  searchRequest.indices("bank");
  //1.2）构造检索条件
  SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
  sourceBuilder.query(QueryBuilders.matchQuery("address","Mill"));

  //1.2.1)按照年龄分布进行聚合
  TermsAggregationBuilder ageAgg= AggregationBuilders.terms("ageAgg").field("age").size(10);
  sourceBuilder.aggregation(ageAgg);

  //1.2.2)计算平均年龄
  AvgAggregationBuilder ageAvg = AggregationBuilders.avg("ageAvg").field("age");
  sourceBuilder.aggregation(ageAvg);
  //1.2.3)计算平均薪资
  AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
  sourceBuilder.aggregation(balanceAvg);

  System.out.println("检索条件："+sourceBuilder);
  searchRequest.source(sourceBuilder);
  //2. 执行检索
  SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
  System.out.println("检索结果："+searchResponse);

  //3. 将检索结果封装为Bean
  SearchHits hits = searchResponse.getHits();
  SearchHit[] searchHits = hits.getHits();
  for (SearchHit searchHit : searchHits) {
    String sourceAsString = searchHit.getSourceAsString();
    Account account = JSON.parseObject(sourceAsString, Account.class);
    System.out.println(account);

  }

  //4. 获取聚合信息
  Aggregations aggregations = searchResponse.getAggregations();

  Terms ageAgg1 = aggregations.get("ageAgg");

  for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
    String keyAsString = bucket.getKeyAsString();
    System.out.println("年龄："+keyAsString+" ==> "+bucket.getDocCount());
  }
  Avg ageAvg1 = aggregations.get("ageAvg");
  System.out.println("平均年龄："+ageAvg1.getValue());

  Avg balanceAvg1 = aggregations.get("balanceAvg");
  System.out.println("平均薪资："+balanceAvg1.getValue());
}
```

结果：

```
检索条件：{"query":{"match":{"address":{"query":"Mill","operator":"OR","prefix_length":0,"max_expansions":50,"fuzzy_transpositions":true,"lenient":false,"zero_terms_query":"NONE","auto_generate_synonyms_phrase_query":true,"boost":1.0}}},"aggregations":{"ageAgg":{"terms":{"field":"age","size":10,"min_doc_count":1,"shard_min_doc_count":0,"show_term_doc_count_error":false,"order":[{"_count":"desc"},{"_key":"asc"}]}},"ageAvg":{"avg":{"field":"age"}},"balanceAvg":{"avg":{"field":"balance"}}}}
检索结果：{"took":1,"timed_out":false,"_shards":{"total":1,"successful":1,"skipped":0,"failed":0},"hits":{"total":{"value":4,"relation":"eq"},"max_score":5.4032025,"hits":[{"_index":"bank","_type":"_doc","_id":"970","_score":5.4032025,"_source":{"account_number":970,"balance":19648,"firstname":"Forbes","lastname":"Wallace","age":28,"gender":"M","address":"990 Mill Road","employer":"Pheast","email":"forbeswallace@pheast.com","city":"Lopezo","state":"AK"}},{"_index":"bank","_type":"_doc","_id":"136","_score":5.4032025,"_source":{"account_number":136,"balance":45801,"firstname":"Winnie","lastname":"Holland","age":38,"gender":"M","address":"198 Mill Lane","employer":"Neteria","email":"winnieholland@neteria.com","city":"Urie","state":"IL"}},{"_index":"bank","_type":"_doc","_id":"345","_score":5.4032025,"_source":{"account_number":345,"balance":9812,"firstname":"Parker","lastname":"Hines","age":38,"gender":"M","address":"715 Mill Avenue","employer":"Baluba","email":"parkerhines@baluba.com","city":"Blackgum","state":"KY"}},{"_index":"bank","_type":"_doc","_id":"472","_score":5.4032025,"_source":{"account_number":472,"balance":25571,"firstname":"Lee","lastname":"Long","age":32,"gender":"F","address":"288 Mill Street","employer":"Comverges","email":"leelong@comverges.com","city":"Movico","state":"MT"}}]},"aggregations":{"lterms#ageAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":38,"doc_count":2},{"key":28,"doc_count":1},{"key":32,"doc_count":1}]},"avg#ageAvg":{"value":34.0},"avg#balanceAvg":{"value":25208.0}}}
ConfigTest.Account(account_number=970, balance=19648, firstname=Forbes, lastname=Wallace, age=28, gender=M, address=990 Mill Road, employer=Pheast, email=forbeswallace@pheast.com, city=Lopezo, state=AK)
ConfigTest.Account(account_number=136, balance=45801, firstname=Winnie, lastname=Holland, age=38, gender=M, address=198 Mill Lane, employer=Neteria, email=winnieholland@neteria.com, city=Urie, state=IL)
ConfigTest.Account(account_number=345, balance=9812, firstname=Parker, lastname=Hines, age=38, gender=M, address=715 Mill Avenue, employer=Baluba, email=parkerhines@baluba.com, city=Blackgum, state=KY)
ConfigTest.Account(account_number=472, balance=25571, firstname=Lee, lastname=Long, age=32, gender=F, address=288 Mill Street, employer=Comverges, email=leelong@comverges.com, city=Movico, state=MT)
年龄：38 ==> 2
年龄：28 ==> 1
年龄：32 ==> 1
平均年龄：34.0
平均薪资：25208.0
```



对应检索语句：

```json

GET /bank/_search
{
  "query": {
    "match": {
      "address": {
        "query": "Mill"
      }
    }
  },
  "aggregations": {
    "ageAgg": {
      "terms": {
        "field": "age",
        "size": 10
      }
    },
    "ageAvg": {
      "avg": {
        "field": "age"
      }
    },
    "balanceAvg": {
      "avg": {
        "field": "balance"
      }
    }
  }
}
```



**注意**

- API 在文档中都能找到：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.x/java-rest-high-getting-started-maven.html

- 高版本和低版本有些差异。

