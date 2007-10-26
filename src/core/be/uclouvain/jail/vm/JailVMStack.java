package be.uclouvain.jail.vm;

import java.util.Stack;

import be.uclouvain.jail.adapt.AdaptUtils;

/**
 * Stack for command invocation.
 * 
 * @author blambeau
 */
public class JailVMStack {

	/** VM stack. */
	private Stack<Object> stack;
	
	/** Creates a stack from values. */
	public JailVMStack(Object[] values) {
		stack = new Stack<Object>();
		int size = values.length;
		for (int i=size-1; i>=0; i--) {
			stack.push(values[i]);
		}
	}
	
	/** Pushes a value on the stack. */
	public void push(Object value) {
		stack.push(value);
	}
	
	/** Pops and return top value from/of the stack. */
	public Object pop() {
		return stack.pop();
	}
	
	/** Returns top value of the stack. */
	public Object top() {
		return stack.peek();
	}
	
	/** Pops an argument from the stack and place it at index-th position 
	 * in the args array. */
	private int popArg(Class<?> type, int offset, Object[] args, int index) throws JailVMException {
		// get from stack
		Object arg = stack.get(stack.size()-offset-1);
		
		// adapt it
		Object adapted = AdaptUtils.adapt(arg,type);
		if (adapted == null) {
			throw new JailVMException("Unable to convert " + arg + " to " + type.getSimpleName());
		}
		
		args[index] = adapted;
		return 1;
	}
	
	/** Pops some arguments of the stack. */
	public Object[] popArgs(Class<?>[] types) throws JailVMException {
		// first check stack size
		int size = types.length;
		if (stack.size() < size) {
			throw new JailVMException("Unexpected stack exception (no args enough)");
		}
		
		// count is the actual number of eat args on stack
		int count = 0;
		
		// try to create args
		Object[] args = new Object[size];
		int i=0;
		for (Class<?> type: types) {
			count += popArg(type,count,args,i++);
		}
		
		// pop all
		for (int j=0; j<count; j++) { pop(); }
		
		return args;
	}
	
}
