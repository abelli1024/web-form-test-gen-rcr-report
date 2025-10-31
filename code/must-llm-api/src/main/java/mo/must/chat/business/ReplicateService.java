package mo.must.chat.business;

import mo.must.chat.dao.ReplicateDAO;
import mo.must.chat.model.ReplicateDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplicateService {
    private final ReplicateDAO replicateDAO;

    public void saveReplicate(ReplicateDO replicateDO) {
        replicateDAO.insert(replicateDO);
    }

    public void updateReplicate(ReplicateDO replicateDO) {
        replicateDAO.updateByPrimaryKey(replicateDO);
    }
    public ReplicateDO getReplicate(Long id) {
        return replicateDAO.selectByPrimaryKey(id);
    }

    public List<ReplicateDO> getNotSucceededReplicateList() {
        Example example = new Example(ReplicateDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("status", "succeeded");
        criteria.andNotEqualTo("status", "failed");
        return replicateDAO.selectByExample(example);
    }
}
