package org.abc.wiki.service;

import org.abc.wiki.websocket.WebSocketServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {

	@Resource
	private WebSocketServer webSocketServer;

	@Async
	public void sendInfo(String msg) {
		webSocketServer.sendInfo(msg);
	}
}
