package ${global.packageName}.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import ${global.packageName}.domain.${entity.className};
import ${global.packageName}.service.${entity.className}Service;
<#assign entityComplex = entity.entityName + "s">
<#if entity.entityName?ends_with("s") ||
	entity.entityName?ends_with("ch") ||
	entity.entityName?ends_with("sh") ||
	entity.entityName?ends_with("x")>
	<#assign entityComplex = entity.entityName + "es">
</#if>
<#if !entity.primaryFields[0].fieldType.canonicalName?starts_with("java.lang.")>
import ${entity.primaryFields[0].fieldType.canonicalName};
</#if>
<#assign idClass = entity.primaryFields[0].fieldType.simpleName>

@RequestMapping("${entityComplex}")
@RestController
public class ${entity.className}Controller {
	
	@Autowired
	private ${entity.className}Service ${entity.entityName}Service;

	@GetMapping
	public IPage<${entity.className}> list(Page pageRequest){
	    return ${entity.entityName}Service.page(pageRequest);
	}
	
	@PostMapping
	public ${entity.className} save(@RequestBody ${entity.className} data){
		${entity.entityName}Service.save(data);
		return data;
	}
	
	@DeleteMapping
	public boolean delete(@RequestBody List<${idClass}> ids){
		return ${entity.entityName}Service.removeByIds(ids);
	}
	
	@PutMapping("/{id}")
	public ${entity.className} update(@PathVariable ${idClass} id, @RequestBody ${entity.className} data){
		<#list entity.primaryFields as primaryField>
		data.set${primaryField.fieldName?cap_first}(id);
		</#list>
		${entity.entityName}Service.updateById(data);
		return data;
	}
	
	@GetMapping("/{id}")
	public ${entity.className} findById(@PathVariable ${idClass} id){
		${entity.className} data = ${entity.entityName}Service.getById(id);
		return data;
	}
	
	@DeleteMapping("/{id}")
	public boolean delete(@PathVariable ${idClass} id){
        return ${entity.entityName}Service.removeById(id);
	}
	
}
