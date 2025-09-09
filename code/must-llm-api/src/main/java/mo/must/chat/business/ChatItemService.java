package mo.must.chat.business;

import mo.must.chat.dao.ChatItemDAO;
import mo.must.chat.dto.ChatItemDTO;
import mo.must.chat.model.ChatItemDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatItemService {
    private final ChatItemDAO chatItemDAO;

    public void saveChatItem(ChatItemDO chatItemDO) {
        chatItemDAO.insert(chatItemDO);
    }

    public void updateChatItem(ChatItemDO chatItemDO) {
        chatItemDAO.updateByPrimaryKey(chatItemDO);
    }
    public ChatItemDO getChatItem(Long id) {
       return chatItemDAO.selectByPrimaryKey(id);
    }


    public List<ChatItemDO> getChatItems(Long sessionId) {
        ChatItemDTO chatItemDTO=new ChatItemDTO();
        chatItemDTO.setSessionId(sessionId);
        return chatItemDAO.getChatItems(chatItemDTO);
    }

}
