package ru.practicum.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.practicum.factory.Factory;
import ru.practicum.model.Publisher;
import ru.practicum.service.publisher.PublisherService;
import ru.practicum.servlet.dto.mapping.PublisherMapper;
import ru.practicum.servlet.dto.mapping.PublisherMapperImpl;
import ru.practicum.servlet.dto.publisher.PublisherRequestDto;
import ru.practicum.servlet.dto.publisher.PublisherResponseDto;
import ru.practicum.servlet.dto.publisher.PublisherResponseDtoShort;
import ru.practicum.servlet.utils.ErrorMessage;
import ru.practicum.servlet.utils.JsonMapper;
import ru.practicum.servlet.utils.Utils;

import java.io.IOException;
import java.util.List;

@WebServlet("/publishers/*")
public class PublisherServlet extends HttpServlet {
    private transient PublisherService service;
    private transient PublisherMapper mapper;

    @Override
    public void init() throws ServletException {
        service = Factory.getPublisherService();
        mapper = new PublisherMapperImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathVar = req.getPathInfo();
        if (pathVar == null || pathVar.isEmpty()) {
            getAllPublishers(resp);
        } else {
            getPublisherByID(resp, Utils.parsePathVariableToId(pathVar));
        }
    }

    private void getAllPublishers(HttpServletResponse resp) throws IOException {
        List<Publisher> publishers = service.getAllPublishers();
        List<PublisherResponseDtoShort> dto = mapper.toPublisherResponseDtoShortList(publishers);
        Utils.settingResponse(resp);
        resp.getWriter().println(JsonMapper.parseToJson(dto));
    }

    private void getPublisherByID(HttpServletResponse resp, long id) throws IOException {
        try {
            Publisher publisher = service.getById(id);
            PublisherResponseDto dto = mapper.toPublisherResponseDto(publisher);
            Utils.settingResponse(resp);
            resp.getWriter().print(JsonMapper.parseToJson(dto));
        } catch (RuntimeException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (IOException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            PublisherRequestDto dto = JsonMapper.parseRequestBody(req, PublisherRequestDto.class);
            Publisher publisher = service.createPublisher(mapper.fromRequestDtoToPublisher(dto));
            resp.setStatus(HttpServletResponse.SC_CREATED);
            Utils.settingResponse(resp);
            resp.getWriter().println(JsonMapper.parseToJson(mapper.toPublisherResponseDtoShort(publisher)));
        } catch (RuntimeException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (IOException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.EMPTY_BODY.msg());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathVar = req.getPathInfo();
        if (pathVar == null || pathVar.isEmpty()) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.EMPTY_PATH_VARIABLE.msg());
            return;
        }
        try {
            PublisherRequestDto dto = JsonMapper.parseRequestBody(req, PublisherRequestDto.class);
            Publisher publisher = service.update(mapper.fromRequestDtoToPublisher(dto),
                    Utils.parsePathVariableToId(pathVar));
            resp.setStatus(HttpServletResponse.SC_OK);
            Utils.settingResponse(resp);
            resp.getWriter().println(JsonMapper.parseToJson(mapper.toPublisherResponseDtoShort(publisher)));
        } catch (RuntimeException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (IOException e) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.EMPTY_BODY.msg());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathVar = req.getPathInfo();
        if (pathVar == null || pathVar.isEmpty()) {
            Utils.setErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ErrorMessage.EMPTY_PATH_VARIABLE.msg());
            return;
        }
        boolean isDeleted = service.deleteById(Utils.parsePathVariableToId(pathVar));
        if (isDeleted) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
