package org.abc.wiki.mapper;

import java.util.List;

import org.abc.wiki.domain.Content;
import org.abc.wiki.domain.ContentExample;
import org.apache.ibatis.annotations.Param;

public interface ContentMapper {
	long countByExample(ContentExample example);

	int deleteByExample(ContentExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Content record);

	int insertSelective(Content record);

	List<Content> selectByExample(ContentExample example);

	Content selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Content record, @Param("example") ContentExample example);

	int updateByExample(@Param("record") Content record, @Param("example") ContentExample example);

	int updateByPrimaryKeySelective(Content record);

	int updateByPrimaryKey(Content record);
}