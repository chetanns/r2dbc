package com.example.r2dbc.controller;

import com.example.r2dbc.model.Account;
import com.example.r2dbc.dao.AccountDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.RestController;



@RestController
public class AccountController {

    private final AccountDAO accountDao;

    public AccountController(AccountDAO accountDao) {
        this.accountDao = accountDao;
    }

    @GetMapping("/accounts/{id}")
    public Mono<ResponseEntity<Account>> getAccount(@PathVariable("id") Long id) {

        return accountDao.findById(id)
                .map(acc -> new ResponseEntity<>(acc, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(null, HttpStatus.NOT_FOUND)));
    }

    @GetMapping("/accounts")
    public Flux<Account> getAllAccounts() {
        return accountDao.findAll();
    }

    @PostMapping("/accounts")
    public Mono<ResponseEntity<Account>> postAccount(@RequestBody Account account) {
        return accountDao.createAccount(account)
                .map(acc -> new ResponseEntity<>(acc, HttpStatus.CREATED))
                .log();
    }
}
