curl -XPUT 'http://127.0.0.1:9200/program' -H 'Content-Type: application/json' -d '
{
  "mappings": {
    "properties": {
      "aspectRatio": {
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
      "audioChannel": {
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
      "color": {
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
      "column": {
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
      "contributor": {
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
      "creator": {
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
      "debutDate": {
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
      "description": {
        "properties": {
          "check": {
            "type": "long"
          },
          "value": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword"
              }
            },
            "analyzer": "ik_max_word"
          }
        }
      },
      "id": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
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
      "menu": {
        "properties": {
          "check": {
            "type": "long"
          },
          "content": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword"
              }
            },
            "analyzer": "ik_max_word"
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
      "programForm": {
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
      "programType": {
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
      "sourceAcquiringMethod": {
        "properties": {
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
      "sourceProvider": {
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
      "system": {
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
            "fields": {
              "keyword": {
                "type": "keyword"
              }
            },
            "analyzer": "ik_max_word"
          }
        }
      }
    }
  }
}'