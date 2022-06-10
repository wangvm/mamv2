curl -XPUT 'http://127.0.0.1:9200/videoinfo' -H 'Content-Type: application/json' -d '
{
  "mappings": {
    "properties": {
      "address": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "aspectRatio": {
        "properties": {
          "height": {
            "type": "long"
          },
          "width": {
            "type": "long"
          }
        }
      },
      "audioChannel": {
        "type": "long"
      },
      "duration": {
        "type": "long"
      },
      "fileName": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        },
        "analyzer": "ik_max_word"
      },
      "frameRate": {
        "type": "float"
      }
    }
  }
}'