package top.lcmatrix.util.codegenerator.common.plugin;

public interface IOutputModel {

	/**
	* do not override
	**/
	default Global getGlobal(){
		return ContextHolder.getGlobal();
	}

	/**
	 * do not override
	 **/
	default Object getInput(){
		return ContextHolder.getInput();
	}
}
