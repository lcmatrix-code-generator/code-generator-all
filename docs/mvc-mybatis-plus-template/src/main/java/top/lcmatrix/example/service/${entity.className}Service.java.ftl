package ${global.packageName}.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${global.packageName}.repository.mapper.${entity.className}Mapper;
import ${global.packageName}.domain.${entity.className};

@Service
public class ${entity.className}Service extends ServiceImpl<${entity.className}Mapper, ${entity.className}> {
	
}
