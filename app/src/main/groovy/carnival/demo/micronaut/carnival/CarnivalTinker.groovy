package carnival.demo.micronaut.carnival



import groovy.util.logging.Slf4j
import jakarta.inject.Singleton
import javax.annotation.PostConstruct
import io.micronaut.context.annotation.Requires

import groovy.transform.CompileStatic

import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource

import carnival.core.graph.CoreGraph
import carnival.core.graph.CoreGraphTinker
import carnival.core.graph.VertexLabelDefinition



@Singleton
@Requires(notEnv="test")
@Requires(property="carnival-demo-micronaut.graph.runtime", value="tinker")
class CarnivalTinkerDefault extends CarnivalTinker {
}


@Singleton
@Requires(env="test")
@Requires(property="carnival-demo-micronaut.graph.test", value="tinker")
class CarnivalTinkerTest extends CarnivalTinker {
}


/**
 * Define a class of objects, CarnivalTinker, that implement Carnival.
 * Micronaut will automatically create a singleton object of this class.
 * The annotation @Singleton is used, but is also the default.
 *
 */
@Slf4j
class CarnivalTinker extends Carnival {

	/** a Carnival core graph */
    CoreGraph coreGraph


    /** no argument constructor that opens an in-memory core graph */
    CarnivalTinker() {
    	coreGraph = CoreGraphTinker.create()
    }


    /**
     * Life-cycle hook to initialize the core graph with the models defined in
     * this project.
     *
     */
    @PostConstruct 
    void initialize() {
        log.trace "\n\n\n\n\nCarnivalTinker\n\n\n\n\n"
        coreGraph.withTraversal { Graph graph, GraphTraversalSource g ->
            String packageName = this.getClass().getPackage().getName()
            coreGraph.initializeGremlinGraph(graph, g, packageName)
        }
    }
    


    /** convenience getter for the underlying gremlin graph */
    Graph getGremlinGraph() {
    	coreGraph.graph
    }
    

    /**
     * Method to reset the core graph, meant to be used only by tests.
     *
     */
    void resetCoreGraph() {
        coreGraph.close()
        this.coreGraph = CoreGraphTinker.create()
        initialize()
    }
}