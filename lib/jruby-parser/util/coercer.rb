module JRubyParser
  module Receiver
    def self.included(cls)
      cls.class_eval do
        def receiver=(value)
           value = value.to_ast_node if value.respond_to? :to_ast_node
           old_value = getReceiver
           value.position = old_value.position unless value.position    
           setReceiver(value)
        end
      end
    end
  end
  
  module Value
    def self.included(cls)
      cls.class_eval do
        def value=(value)
           value = value.to_ast_node if value.respond_to? :to_ast_node
           old_value = getValue
           value.position = old_value.position unless value.position    
           setValue(value)
        end
      end
    end
  end  
  
  module Args
    def self.included(cls)
      cls.class_eval do
        def args=(value)
           value = value.to_ast_node if value.respond_to? :to_ast_node
           old_value = getArgs
           unless value.position
             value.position = old_value.position 
             value.each { |e| e.position = old_value.position } #if value.respond_to? :each
           end
           
           setArgs(value)
        end
      end
    end
  end
end