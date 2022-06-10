curl -XPUT "http://127.0.0.1:9200/scenes" -H 'Content-Type: application/json' -d '
{
  "mappings": {
    "properties": {
      "description": {
        "properties": {
          "check": {
            "type": "long"
          },
          "value": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          }
        }
      },
      "menu": {
        "properties": {
          "check": {
            "type": "long"
          },
          "content": {
            "type": "text",
            "analyzer": "ik_max_word",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "id": {
            "type": "long"
          },
          "level": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "parent": {
            "type": "long"
          }
        }
      },
      "outPoint": {
        "properties": {
          "check": {
            "type": "long"
          },
          "value": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          }
        }
      },
      "startPoint": {
        "properties": {
          "check": {
            "type": "long"
          },
          "value": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          }
        }
      },
      "keyFrames": {
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
          "check": {
            "type": "long"
          },
          "description": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          }
        }
      },
      "subtitleForm": {
        "properties": {
          "check": {
            "type": "long"
          },
          "value": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          }
        }
      },
      "taskId": {
        "type": "long"
      },
      "title": {
        "properties": {
          "check": {
            "type": "long"
          },
          "value": {
            "type": "text",
            "analyzer": "ik_max_word",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          }
        }
      }
    }
  }
}
'