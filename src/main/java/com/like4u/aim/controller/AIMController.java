package com.like4u.aim.controller;

import com.like4u.aim.domain.Message;
import com.like4u.aim.domain.MessageType;
import com.like4u.aim.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/15 18:51
 */
@Controller
public class AIMController {
    private List<Message> messages=new ArrayList<>();
    @Autowired
    private UserService userService;
    @Autowired
    private UserClientService userClientService;
    @Autowired
    private MessageClientService messageClientService;
    @Autowired
    private FileClientService fileClientService;

    @GetMapping("/login")
    public String toLogin(){
        return "login";
    }

    @PostMapping("/logincheck")
    public String loginCheck(@RequestParam("username") String username,
            @RequestParam("password") String password, Model model){


        String msg = userService.preCheck(username, password);
        if (msg!=null){
            model.addAttribute("msg",msg);
            return "login";
        }

        if(userClientService.check(username,password)){
            model.addAttribute("msg",username);
            return "main";
        }

        else {
            model.addAttribute("msg","用户名或密码错误");
            return "login";}

    }
    @GetMapping("/main")
    public String toMain(@RequestParam("msg") String sender, Model model){
        model.addAttribute("msg",sender);
        return "main";
    }


    @ResponseBody
    @GetMapping("/success")
    public String success(){
        return "success";
    }

    @GetMapping("/userlist")
    private String getUserList(@RequestParam("msg") String msg, Model model){

        userClientService.getUserList(msg);
        Message message=null;
        while (message==null|| !Objects.equals(message.getMessageType(), MessageType.MESSAGE_USER_LIST)){
            message = ManageClientConnectServerThread.getClientConnectServerThread(msg).getMessage();
        }

        String content = message.getContent();
        String[] userList = content.split(" ");
        model.addAttribute("userList",userList);
        model.addAttribute("msg",msg);

        return "userList";
    }
    @GetMapping("/logout")
    public String logout(@RequestParam("msg") String username){
        userClientService.logout(username);
        return "login";

    }
    @GetMapping("/message")
    public String message(@RequestParam("msg") String username){

        return "forward:userList";
    }
    @PostMapping("/sendmessage")
    public String message(Model model,@RequestParam("sender") String sender,
                          @RequestParam("getter") String getter){

        model.addAttribute("sender",sender);
        model.addAttribute("getter",getter);
        return "chatting";

    }
    @PostMapping("/chatting")
    public String chatting(Model model,
                           @RequestParam("sender") String sender,
                           @RequestParam("getter") String getter,
                           @RequestParam(value = "content",required = false)String context){

        if (StringUtils.hasLength(context)){
            messageClientService.sendMessageToOne(sender,context,getter);
        }

        /**
         * 通过前台传来的sender（其实是一堆请求共享来的）来获取用户对应的线程，得到线程里持有的消息
         * 如果消息不是空的，是个普通消息我就加到消息队列里面
         * 每提交一次表单都是一次判断
         * */

        Message message=null;
        if (ManageClientConnectServerThread.getClientConnectServerThread(sender).getCommonMessage()!=null){
           message =ManageClientConnectServerThread.getClientConnectServerThread(sender).getCommonMessage();
        }

        //socket读取的消息是空的或者读取的消息不是普通消息,而且你发送的消息不是空的，就可以吧你的消息放进队列
        if (message!=null){
            messages.add(message);
            ManageClientConnectServerThread.getClientConnectServerThread(sender).setCommonMessage(null);
           /* Message mymsg = new Message(sender, getter, context, null, MessageType.MESSAGE_COMMON);
            messages.add(mymsg);*/
        }


        model.addAttribute("messages",messages);
        model.addAttribute("sender",sender);
        model.addAttribute("getter",getter);
        return "chatting" ;
    }
    @PostMapping("/broadcast")
    public String  broadcast(Model model,
                             @RequestParam("sender") String sender,
                             @RequestParam("getter") String getter,
                             @RequestParam(value = "content",required = false)String context){


        messageClientService.sendMessageToAll(sender,context);

        model.addAttribute("sender",sender);
        model.addAttribute("getter",getter);

        return "forward:chatting";
    }

    @ResponseBody
    @PostMapping("sendMessage")
    public String sendMessage(@RequestBody com.like4u.aim.vo.Message msg ){

        if (!fileClientService.isFileExit(msg.getSrc())){
            return "他奶奶滴，发我不存在的文件是吧";
        }

        fileClientService.sendFileToOne(msg.getSender(),msg.getGetter(),msg.getSrc());

        return "success";
    }

}
