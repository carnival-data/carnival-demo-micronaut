package carnival.demo.micronaut



import groovy.util.logging.Slf4j
import jakarta.inject.Inject
import jakarta.inject.Singleton
import groovy.transform.ToString

import carnival.demo.micronaut.carnival.Carnival



@ToString(includeNames=true)
@Slf4j 
@Singleton
class DemoService { 

    @Inject Carnival carnival
    @Inject DemoVine demoVine
    @Inject DemoMethods demoMethods

    /** */
    public int totalVertices() {
        def totalVertices
        carnival.coreGraph.withTraversal { graph, g ->
            totalVertices = g.V().count().next()
        }
        totalVertices
    }


    /** */
    public void sayHi() {
        def mdt = demoVine.method('People').call().result
        mdt.dataIterator().each {
            println "Hi ${it.FIRST} ${it.LAST}!"
        }
    }


    /** */
    public void buildGraph() {
        carnival.coreGraph.withTraversal { graph, g ->
            demoMethods.method('LoadPeople').call(graph, g)
        }
    }

}