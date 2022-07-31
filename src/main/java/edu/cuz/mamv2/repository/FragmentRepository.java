package edu.cuz.mamv2.repository;

import edu.cuz.mamv2.entity.MamFragment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author VM
 * @date 2022/3/4 21:55
 */
public interface FragmentRepository extends ElasticsearchRepository<MamFragment, String> {
}
