def globals = [:]
globals << [jg_memory_ts : traversal().withEmbedded(jg_memory).withStrategies(ReferenceElementStrategy)]
