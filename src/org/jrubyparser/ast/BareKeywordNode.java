package org.jrubyparser.ast;

import java.util.List;
import org.jrubyparser.SourcePosition;
import static org.jrubyparser.ast.Node.EMPTY_LIST;

/**
 * Ruby keywords like self,true,false,nil.
 */
public abstract class BareKeywordNode extends Node implements INameNode {
    private String name;
    
    public BareKeywordNode(SourcePosition position, String name) {
        super(position);
     
        this.name = name;
    }
    
    public List<Node> childNodes() {
        return EMPTY_LIST;
    }
    
    /**
     * Get name of self node.
     */
    public String getName() {
        return name;
    }
    
    public void setName(String newName) {
        // FIXME: error or noop?
    }
    
    public boolean isNameMatch(String testName) {
        return name.equals(testName);
    }
}
