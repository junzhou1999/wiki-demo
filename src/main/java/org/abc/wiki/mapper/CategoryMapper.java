package org.abc.wiki.mapper;

import java.util.List;

import org.abc.wiki.domain.Category;
import org.abc.wiki.domain.CategoryExample;
import org.apache.ibatis.annotations.Param;

public interface CategoryMapper {
	long countByExample(CategoryExample example);

	int deleteByExample(CategoryExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Category record);

	int insertSelective(Category record);

	List<Category> selectByExample(CategoryExample example);

	Category selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Category record, @Param("example") CategoryExample example);

	int updateByExample(@Param("record") Category record, @Param("example") CategoryExample example);

	int updateByPrimaryKeySelective(Category record);

	int updateByPrimaryKey(Category record);
}