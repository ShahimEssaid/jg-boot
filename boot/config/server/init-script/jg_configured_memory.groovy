def globals = [:]
globals << [jg_configured_memory_ts : traversal().withEmbedded(jg_configured_memory).withStrategies(ReferenceElementStrategy)]
