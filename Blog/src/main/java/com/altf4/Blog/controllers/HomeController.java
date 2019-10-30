/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.altf4.Blog.controllers;

import com.altf4.Blog.dao.MemoDao;
import com.altf4.Blog.dao.RoleDao;
import com.altf4.Blog.dao.TagDao;
import com.altf4.Blog.dto.User;
import com.altf4.Blog.dto.Memo;
import com.altf4.Blog.dto.Tag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.altf4.Blog.dao.UserDao;

/**
 *
 * @author mike
 */
@Controller
public class HomeController {

    @Autowired
    UserDao userDao;

    @Autowired
    MemoDao memoDao;

    @Autowired
    TagDao tagDao;

    @Autowired
    RoleDao roleDao;

    @GetMapping("")
    public String getIndex(Model model) {
        return displayRecentMemos(model);
    }

    @GetMapping("/home")
    public String displayRecentMemos(Model model) {
        List<Memo> allPosts = memoDao.getAll();
        List<Tag> allTags = tagDao.getAll();
        List<User> allUsers = userDao.getAllUsers();
        List<Memo> recentPosts = new ArrayList<>();

        List<Memo> approvedPosts = memoDao.getAllApproved();

        for (Memo m : approvedPosts) {
            if (m.getBodyText().length() > 100) {
                m.setBodyText(reduceTextBody(m.getBodyText()) + "...");
            }
        }

        if (approvedPosts.size() > 10) {
            for (int i = 1; i <= 10; i++) {
                Memo post = approvedPosts.get(approvedPosts.size() - i);
                recentPosts.add(post);
            }
        } else {
            for (Memo post : approvedPosts) {

                recentPosts.add(0, post);
            }
        }

        model.addAttribute("allPosts", allPosts);
        model.addAttribute("allTags", allTags);
        model.addAttribute("allCreators", allUsers);
        model.addAttribute("recentPosts", recentPosts);

        return "home";
    }

    private String reduceTextBody(String body) {
        String newBody = body.substring(0, 100);
        return newBody;
    }

}
