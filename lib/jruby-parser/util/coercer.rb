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
end