package ${global.packageName}.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

<#assign fieldTypeForImport={}>
<#list entity.fields as field>
	<#if !field.fieldType.canonicalName?starts_with("java.lang.")>
		<#assign fieldTypeForImport = fieldTypeForImport + {field.fieldType.canonicalName : 1}>
	</#if>
	<#if field.fieldType.canonicalName == "java.util.Date">
		<#assign fieldTypeForImport = fieldTypeForImport + {"org.springframework.format.annotation.DateTimeFormat" : 1}>
		<#assign fieldTypeForImport = fieldTypeForImport + {"com.alibaba.fastjson.annotation.JSONField" : 1}>
	</#if>
</#list>
<#list fieldTypeForImport?keys as fieldType>
import ${fieldType};
</#list>

/**
 * 
 * ${entity.description!}
 *
 */
@Data
@TableName("${entity.name}")
public class ${entity.className} {
	private static final long serialVersionUID = 1L;
	
	<#list entity.fields as field>
		<#if field.enums!?size gt 0>
			<#list field.enums as enum>
	/**
	 * ${enum.text}
	 */
				<#if field.ofTextType>
	public static final ${field.fieldType.baseName} ${(field.name + "_" + enum.text)?upper_case} = "${enum.value}";
				<#else>
	public static final ${field.fieldType.baseName} ${(field.name + "_" + enum.text)?upper_case} = ${enum.value};
				</#if>
			</#list>
		</#if>
	</#list>
	
	<#list entity.fields as field>
       <#if (field.description!"") != "">
    /**
     * ${field.description}
     */
       </#if>
       <#if field.primaryKey>
    @TableId(value= "${field.name}"<#if field.autoIncrement>, type = IdType.AUTO</#if>)
       </#if>
       <#if field.fieldType.canonicalName == "java.util.Date">
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
       </#if>
    @TableField("${field.name}")
    private ${field.fieldType.simpleName} ${field.fieldName};

	</#list>
}