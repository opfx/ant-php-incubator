<?php
spl_autoload_register(
    function($class) {
        static $classes = null;
        if ($classes === null) {
            $classes = array(
                ___CLASSLIST___
            );
        }
        $cn = strtolower($class);
        if (isset($classes[$cn])) {
            require 'phar://example-test.phar' . $classes[$cn];
        }
    },
    true,
    false
);
Phar::mapPhar('example-test.phar');
__HALT_COMPILER(); ?>