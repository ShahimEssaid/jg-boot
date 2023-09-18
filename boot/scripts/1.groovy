map = new HashMap<String, Object>();
map.put("storage.backend", "inmemory");
map.put("graph.graphname", "graph1");
ConfiguredGraphFactory.createConfiguration(new org.apache.commons.configuration2.MapConfiguration(map));



System.out.println("=========== HELLO  ========= ");