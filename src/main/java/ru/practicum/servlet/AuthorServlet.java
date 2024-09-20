package ru.practicum.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.practicum.factory.Factory;
import ru.practicum.model.Author;
import ru.practicum.service.author.AuthorService;
import ru.practicum.servlet.dto.author.AuthorRequestDto;
import ru.practicum.servlet.dto.author.AuthorResponseDto;
import ru.practicum.servlet.dto.author.AuthorResponseDtoShort;
import ru.practicum.servlet.dto.mapping.AuthorMapper;
import ru.practicum.servlet.dto.mapping.AuthorMapperImpl;
import ru.practicum.servlet.utils.ErrorMessage;
import ru.practicum.servlet.utils.JsonMapper;
import ru.practicum.servlet.utils.Utils;

import java.io.IOException;
import java.util.List;

@WebServlet("/authors/*")
public class AuthorServlet extends HttpServlet {
    private transient AuthorService authorService;
    private transient AuthorMapper mapper;

    @Override
    public void init() throws ServletException {
        authorService = Factory.getAuthorService();
        mapper = new AuthorMapperImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathVar = req.getPathInfo();
        if (pathVar == null || pathVar.isEmpty()) {
            getAllAuthor(resp);
        } else {
            getAuthorById(resp, Utils.parsePathVariableToId(pathVar));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            AuthorRequestDto dto = JsonMapper.parseRequestBody(req, AuthorRequestDto.class);
            Author author = authorService.create(mapper.fromAuthorRequestDto(dto));
            resp.setStatus(HttpServletResponse.SC_CREATED);
            Utils.settingResponse(resp);
            resp.getWriter().println(JsonMapper.parseToJson(mapper.authorToResponseDtoShort(author)));
        } catch (RuntimeException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (IOException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.EMPTY_BODY.msg());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathVar = req.getPathInfo();
        if (pathVar == null || pathVar.isEmpty()) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.EMPTY_PATH_VARIABLE.msg());
            return;
        }
        boolean isDeleted = authorService.deleteById(Utils.parsePathVariableToId(pathVar));
        if (isDeleted) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathVar = req.getPathInfo();
        try {
            if (pathVar == null || pathVar.isEmpty()) {
                Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.EMPTY_PATH_VARIABLE.msg());
                return;
            }
            AuthorRequestDto dto = JsonMapper.parseRequestBody(req, AuthorRequestDto.class);
            Author author = authorService.update(mapper.fromAuthorRequestDto(dto),
                    Utils.parsePathVariableToId(pathVar));
            resp.setStatus(HttpServletResponse.SC_OK);
            Utils.settingResponse(resp);
            resp.getWriter().println(JsonMapper.parseToJson(mapper.authorToResponseDtoShort(author)));
        } catch (RuntimeException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (IOException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.EMPTY_BODY.msg());
        }
    }

    private void getAuthorById(HttpServletResponse resp, long id) throws IOException {
        try {
            Author author = authorService.getById(id);
            AuthorResponseDto dto = mapper.authorToResponseDto(author);
            Utils.settingResponse(resp);
            resp.getWriter().print(JsonMapper.parseToJson(dto));
        } catch (RuntimeException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }


    private void getAllAuthor(HttpServletResponse resp) throws IOException {
        List<Author> authorList = authorService.getAll();
        List<AuthorResponseDtoShort> dtoList = mapper.authorListToResponseDtoShortList(authorList);
        Utils.settingResponse(resp);
        resp.getWriter().print(JsonMapper.parseToJson(dtoList));
    }


}
