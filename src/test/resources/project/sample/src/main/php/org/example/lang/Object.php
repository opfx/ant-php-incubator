<?php
namespace org\example\lang;

class Object {
	
	protected function __construct() {
		;
	}
	
	public function __destruct() {
	
	}
	
	public function getClass() {
		static $class = null;
		if($class === null ) {
			$class = new ReflectionClass($this);
		}
		return $class;
	}
	
}