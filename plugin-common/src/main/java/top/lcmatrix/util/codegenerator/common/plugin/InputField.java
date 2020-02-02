package top.lcmatrix.util.codegenerator.common.plugin;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InputField {
    String label() default "";
    boolean required() default false;
    String validateRegExp() default "";
    String defaultValue() default "";

    /**
    * only for string field, if is a password field, set mask to true to mask the field content with "*"
    * @author haifeng.peng
    * @date 2020/1/8 9:39
    **/
    boolean mask() default false;

    /**
    * only for file
     *<p> FILES_ONLY = 0; </p>
     *<p> DIRECTORIES_ONLY = 1; </p>
     *<p> FILES_AND_DIRECTORIES = 2; </p>
    * @author haifeng.peng
    * @date 2020/1/4 13:58
    **/
    int fileSelectionMode() default 2;

    /**
    * only for file,separate by ","
    * @author haifeng.peng
    * @date 2020/1/4 14:04
    **/
    String allowFileSuffixes() default "";

    static int FILE_SELECTION_MODE_FILES_ONLY = 0;
    static int FILE_SELECTION_MODE_DIRECTORIES_ONLY = 1;
    static int FILE_SELECTION_MODE_FILES_AND_DIRECTORIES = 2;
}
