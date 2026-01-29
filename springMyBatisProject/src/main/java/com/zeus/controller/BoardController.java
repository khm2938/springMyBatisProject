package com.zeus.controller;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zeus.domain.Board;
import com.zeus.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/board")
@MapperScan(basePackages = "com.zeus.mapper")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/insertForm")
	public String boardInsertForm(Model model) {
		return "board/insertForm";
	}

	@PostMapping("/insert")
	public String postMethodName(Board board, Model model) {
		log.info("insert board =" + board.toString());
		try {
			boardService.create(board);
		} catch (Exception e) {
			e.printStackTrace();
			return "board/failed";
		}
		model.addAttribute("message", "%s님 회원정보가 등록되었습니다.".formatted(board.getWriter()));
		return "board/success";
	}

	@GetMapping("/boardList")
	public String boardList(Model model) {
		log.info("boardList");
		try {
			List<Board> boardList = boardService.list();
			model.addAttribute("boardList", boardList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "board/boardList";
	}

	@GetMapping("/detail")
	public String boardDetail(Board b, Model model) {
		log.info("BoardDetail board: " + b.toString());
		try {
			Board board = boardService.read(b);
			if (board == null) {
				model.addAttribute("message", "%d 님의 상세정보가 없습니다.".formatted(board.getNo()));
				return "board/failed";
			}
			model.addAttribute("board", board);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "board/detail";
	}

	@GetMapping("/delete")
	public String boardDelete(Board board, Model model) {
		log.info("boardDelete board= " + board.toString());
		try {
			boardService.delete(board);
			model.addAttribute("board", board);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "%s 님의 게시글 삭제실패하였습니다.".formatted(board.getWriter()));
			return "board/failed";
		}
		model.addAttribute("message", "%d번 게시글이 삭제되었습니다.".formatted(board.getNo()));
		return "board/success";
	}

	@GetMapping("/updateForm")
	public String boardUpdateForm(Board b, Model model) {
		log.info("boardUpdateForm board= " + b.toString());
		try {
			Board board = boardService.read(b);
			if (board == null) {
				model.addAttribute("message", "%d 님의 정보가 없습니다.".formatted(b));
				return "board/failed";
			}
			model.addAttribute("board", board);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "board/updateForm";
	}

	@PostMapping("/update")
	public String boardUpdate(Board b, Model model) { 
		log.info("boardUpdate board =" + b.toString());
		try {
			boardService.update(b);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "%s님의 게시글 수정실패".formatted(b.getWriter()));
			return "board/failed";
		}
		model.addAttribute("message", "%s님의 게시글 수정성공".formatted(b.getWriter()));
		return "board/success";
	}
	
	@GetMapping("/search")
	public String boardSearch(Board board, Model model) {
	    log.info("boardSearch board"+board.toString());
	    try {
	        List<Board> boardList = boardService.search(board);
	        model.addAttribute("boardList", boardList);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return "board/boardList";
	}
	
}

