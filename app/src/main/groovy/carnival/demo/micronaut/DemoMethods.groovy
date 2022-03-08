package carnival.demo.micronaut



import groovy.util.logging.Slf4j
import jakarta.inject.Inject
import jakarta.inject.Singleton
import groovy.transform.ToString

import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import carnival.core.graph.GraphMethods
import carnival.core.graph.GraphMethod

import carnival.demo.micronaut.carnival.Carnival



@ToString(includeNames=true)
@Slf4j 
@Singleton
class DemoMethods implements GraphMethods { 


    Carnival carnival
    DemoVine demoVine


    public DemoMethods(Carnival carnival, DemoVine demoVine) {
        this.carnival = carnival
        this.demoVine = demoVine
    }


    /** */
    class LoadPeople extends GraphMethod {

        void execute(Graph graph, GraphTraversalSource g) {

            def mdt = demoVine
                .method('People')
                .call()
            .result

            mdt.data.values().each { rec ->
                log.trace "rec: ${rec}"

                def first = rec.FIRST?.trim()
                def last = rec.LAST?.trim()

                if (!(first || last)) return

                def personV = DemoModel.VX.PERSON.instance().create(graph)
                def nameV = DemoModel.VX.NAME.instance().withNonNullProperties(
                    DemoModel.PX.FIRST, first,
                    DemoModel.PX.LAST, last
                ).ensure(graph, g)
                DemoModel.EX.IS_CALLED.instance().from(personV).to(nameV).create()
            }

        }

    }


}