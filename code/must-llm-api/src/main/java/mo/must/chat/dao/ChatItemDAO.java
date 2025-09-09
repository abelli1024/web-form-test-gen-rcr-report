package mo.must.chat.dao;


import mo.must.chat.common.config.MyMapper;
import mo.must.chat.dto.ChatItemDTO;
import mo.must.chat.model.ChatItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatItemDAO extends MyMapper<ChatItemDO> {
    public List<ChatItemDO> getChatItemList(ChatItemDTO chatItemDTO);

    public List<ChatItemDO> getChatItems(ChatItemDTO chatItemDTO);


}
