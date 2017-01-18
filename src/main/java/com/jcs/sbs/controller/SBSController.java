package com.jcs.sbs.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.hibernate.HibernateException;

import com.google.gson.Gson;
import com.jcs.sbs.common.CommonResponse;
import com.jcs.sbs.common.Util;
import com.jcs.sbs.service.CommonService;

@Path("/sbs")
public class SBSController {
    @GET
    @Produces("application/json")
    public Response getResult(@QueryParam("queryType") String queryType, @QueryParam("search") String search,
            @QueryParam("sortBy") String sortBy, @QueryParam("sortDirection") String sortDirection,
            @QueryParam("offset") int offset, @QueryParam("limit") int limit, @QueryParam("filter") String filter,
            @QueryParam("optional") List<String> optionalParams) {
        try {
            CommonService service = Util.getService(queryType);
            if (service == null) {
                return Response.status(400).entity("Invalid queryType").build();
            }

            if (search == null) {
                search = "";
            }
            CommonResponse result = new CommonResponse();
            result.setTableHeaders(Util.getTableHeaders(queryType));
            result.setKeys(new ArrayList<>(result.getTableHeaders().keySet()));

            if (sortBy == null) {
                switch (queryType) {
                case "accountSummary":
                    sortBy = "project_id";
                    break;
                case "volume":
                case "snapshot":
                    sortBy = "id";
                    break;
                }
            }
            System.out.println(optionalParams.toString());
            result.setTableData(service.getResult(search, sortBy, sortDirection, offset, limit, filter,optionalParams));
            result.setTotalResults(service.getTotalResultCount(search, filter, optionalParams).getTotalResults());

            Gson gson = new Gson();
            String output = gson.toJson(result);
            return Response.status(200).entity(output).build();
        } catch (HibernateException e) {
            return Response.status(503).entity(e.toString()).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.toString()).build();
        }
    }

}
