package com.dingtalk;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiImChatScencegroupMessageSendV2Request;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiImChatScencegroupMessageSendV2Response;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.dingtalk.constant.AppConstant;
import com.dingtalk.constant.UrlConstant;
import com.dingtalk.util.AccessTokenUtil;
import com.taobao.api.ApiException;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

/**
 * 实现了机器人的简单问答功能
 */
@RestController
public class RobotsController {

    private static final String cardMSG = "测试卡片消息";
    private static final String Hello = "你好";
    private static final String DrawLots = "抽签";




    @RequestMapping(value = "/robots", method = RequestMethod.POST)
    public String helloRobots(@RequestBody(required = false) JSONObject json) {
        System.out.println(json);
        String content = json.getJSONObject("text").get("content").toString().replaceAll(" ", "");
        // 获取用户手机号，用于发送@消息
        // String mobile = getUserMobile(json.getString("senderStaffId"));
        String sessionWebhook = json.getString("sessionWebhook");
        DingTalkClient client = new DefaultDingTalkClient(sessionWebhook);

        if (Hello.contains(content)) {
            hello(client);
        } else if(DrawLots.contains(content)){
            drawLots(client);
        }else {
            learning(client);
        }
        return null;
    }
    /**
     * 抽签
     */
    private void drawLots(DingTalkClient client) {
        try {
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            //随机抽签
            String[] branches = {"斯莱特林","格兰芬多","拉文克劳","赫奇帕奇"};
            Random r = new Random(System.currentTimeMillis());
            int  i = r.nextInt(branches.length);
            text.setContent(branches[i]);
            request.setText(text);
            // OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            // at.setAtMobiles(Arrays.asList(mobile));
            // isAtAll类型如果不为Boolean，请升级至最新SDK
            // at.setIsAtAll(false);
            // request.setAt(at);
            OapiRobotSendResponse response = client.execute(request);
            System.out.println(response.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }



    /**
     * 回答其他
     */
    private void learning(DingTalkClient client) {
        try {
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent("我是问好机器人 ~");
            request.setText(text);
            // OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            // at.setAtMobiles(Arrays.asList(mobile));
            // isAtAll类型如果不为Boolean，请升级至最新SDK
            // at.setIsAtAll(false);
            // request.setAt(at);
            OapiRobotSendResponse response = client.execute(request);
            System.out.println(response.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用发送群助手消息接口发送卡片消息
     */
    public String actionCardMsg() {
        try {
            String accessToken = AccessTokenUtil.getAccessToken();
            DingTalkClient client = new DefaultDingTalkClient(UrlConstant.SCENCEGROUP_MESSAGE_SEND_V2);
            OapiImChatScencegroupMessageSendV2Request req = new OapiImChatScencegroupMessageSendV2Request();
            req.setTargetOpenConversationId(AppConstant.OPEN_CONVERSATION_ID);
            // 这个消息模板ID是 官方通用测试模板
            req.setMsgTemplateId("offical_template_test_action_card");
            req.setMsgParamMapString("{     " +
                "\"title\": \"会话列表显示的标题\"," +
                "\"markdown\": \"# 消息正文标题\"," +
                "\"btn_orientation\": \"2\"," +
                "\"btn_title_1\": \"按钮一号\"," +
                "\"action_url_1\": \"www.dingtalk.com\"," +
                "\"btn_title_2\": \"按钮二号\"," +
                "\"action_url_2\": \"www.dingtalk.com\"}");
            req.setRobotCode(AppConstant.ROBOT_CODE);
            OapiImChatScencegroupMessageSendV2Response rsp = client.execute(req, accessToken);
            System.out.println(rsp.getBody());
            return rsp.getBody();
        } catch (ApiException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }



    /**
     * 回答你好
     */
    public void hello(DingTalkClient client) {
        try {
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent("你也好 ~");
            request.setText(text);
            // OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            // at.setAtMobiles(Arrays.asList(mobile));
            // isAtAll类型如果不为Boolean，请升级至最新SDK
            // at.setIsAtAll(false);
            // request.setAt(at);
            OapiRobotSendResponse response = client.execute(request);
            System.out.println(response.getBody());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
