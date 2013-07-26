module JRubyParser
  module Receiver
    def self.included(cls)
      cls.class_eval do
        def receiver=(value)
          old_value = getReceiver
          if value.respond_to? :to_ast_node
            value = value.to_ast_node(old_value.position) 
          end
          setReceiver(value)
        end
      end
    end
  end
  
  module Value
    def self.included(cls)
      cls.class_eval do
        def value=(value)
          old_value = getValue
          if value.respond_to? :to_ast_node
            value = value.to_ast_node(old_value.position) 
          end

          setValue(value)
        end
      end
    end
  end  
  
  module Args
    def self.included(cls)
      cls.class_eval do
        def args=(value)
          old_value = getArgs
          if value.respond_to? :to_ast_node
            value = value.to_ast_node(old_value.position) 
          end

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
