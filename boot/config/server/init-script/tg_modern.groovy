org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory.generateModern(tg_modern)

def globals = [:]
def ts = traversal().withEmbedded(tg_modern).withStrategies(ReferenceElementStrategy)
globals << [tg_modern_ts : ts]
