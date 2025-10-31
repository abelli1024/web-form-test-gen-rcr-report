package mo.must.chat.manager.domain.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptRequestParameter {

    String model =null;

    List<ChatGptMessage> messages = new ArrayList<>();

    public void addModel(String model) {
        this.model=model;
    }
    public void addMessages(ChatGptMessage message) {
        this.messages.add(message);
    }
    public void addMessages(List<ChatGptMessage> messages) {
        this.messages=messages;
    }
}
