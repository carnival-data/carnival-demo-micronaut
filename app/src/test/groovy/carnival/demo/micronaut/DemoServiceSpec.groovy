package carnival.demo.micronaut


import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import spock.lang.Shared
import jakarta.inject.Inject
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource

import carnival.core.graph.Core
import carnival.demo.micronaut.carnival.Carnival



@MicronautTest
class DemoServiceSpec extends Specification {


    ///////////////////////////////////////////////////////////////////////////
    // SHARED FIELDS
    ///////////////////////////////////////////////////////////////////////////

    @Inject EmbeddedApplication<?> application
    
    @Shared @Inject DemoService demoService
    @Shared @Inject Carnival carnival
    @Shared Graph graph
    @Shared GraphTraversalSource g


    ///////////////////////////////////////////////////////////////////////////
    // TEST SET-UP
    ///////////////////////////////////////////////////////////////////////////
    def setupSpec() {
        carnival.resetCoreGraph()
        graph = carnival.coreGraph.graph
    }

    def setup() {
        g = carnival.coreGraph.traversal()
    }

    def cleanup() {
        if (g) g.close()
        if (graph?.features().graph().supportsTransactions()) {
            graph.tx().rollback()
        } else {
            carnival.resetCoreGraph()
            graph = carnival.coreGraph.graph
        }
    }

    def cleanupSpec() {
        if (graph) graph.close()
    }


    ///////////////////////////////////////////////////////////////////////////
    // TESTS
    ///////////////////////////////////////////////////////////////////////////

    void 'build graph'() {
        expect:
        g.V().isa(DemoModel.VX.PERSON).count().next() == 0
        g.V().isa(Core.VX.GRAPH_PROCESS).count().next() == 0

        when:
        demoService.buildGraph()
        g.V().each { println "${it.label()}" }

        then:
        g.V().isa(DemoModel.VX.PERSON).count().next() == 2
        g.V()
            .isa(DemoModel.VX.PERSON)
            .out(DemoModel.EX.IS_CALLED)
            .isa(DemoModel.VX.NAME)
            .has(DemoModel.PX.FIRST, 'Alice')
        .count().next() == 1
        g.V().isa(Core.VX.GRAPH_PROCESS).count().next() == 1
    }


    void 'say hi'() {
        when:
        demoService.sayHi()

        then:
        noExceptionThrown()
    }


    void 'total vertices'() {
        expect:
        carnival != null

        when:
        def tv1 = demoService.totalVertices()

        then:
        tv1 >= 0

        when:
        graph.addVertex('TestVertex')
        def tv2 = demoService.totalVertices()

        then:
        tv2 == tv1 + 1
    }

}