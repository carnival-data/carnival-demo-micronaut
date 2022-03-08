package carnival.demo.micronaut.carnival



import org.apache.tinkerpop.gremlin.structure.Graph
import carnival.core.graph.CoreGraph



/**
 * The Carnival interface wraps a CoreGraph object.
 *
 * Defines an interface that Micronaut can use to create a bean, which by
 * default will be a singleton.
 *
 */
abstract class Carnival {

	/** the core graph object */
    abstract CoreGraph getCoreGraph()

    /** */
    abstract Graph getGremlinGraph()
    
    /** */
    abstract void resetCoreGraph()

    /** */
    boolean supportsTransactions() {
        coreGraph.graph.features().graph().supportsTransactions()        
    }

    /** Convenience */
    void commit() {
        if (supportsTransactions()) {
            coreGraph.graph.tx().commit()
        }
    }

}