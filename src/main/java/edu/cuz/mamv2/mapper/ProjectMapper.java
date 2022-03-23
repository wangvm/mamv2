package edu.cuz.mamv2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.cuz.mamv2.entity.Project;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 媒资项目表 Mapper 接口
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * <p>
     * 查询指定用户的所有参与项目
     * </p>
     * @param page
     * @param account
     * @return
     */
    Page<Project> selectUserProjectsPage(Page<?> page, @Param("account") Integer account);
}
