package com.jcs.sbs.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.HibernateException;

import com.google.gson.Gson;
import com.jcs.sbs.common.Cache;
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
            @QueryParam("optional") List<String> optionalParams, @Context UriInfo uriInfo) {
        try {

            Gson gson = new Gson();
            String queryParams = gson.toJson(uriInfo.getQueryParameters());

            String cachedData = Cache.getData(queryParams);
            if (Cache.getData(queryParams) != null) {
                return Response.status(200).entity(cachedData).build();
            }

            CommonService service = Util.getService(queryType);
            if (service == null) {
                return Response.status(400).entity("Invalid queryType").build();
            }

            if (search == null) {
                search = "";
            }

            if (search.length() > 0 && Util.isFieldBoolean(queryType, filter)) {
                if ("true".contains(search)) {
                    search = "1";
                } else if ("false".contains(search)) {
                    search = "0";
                }
            }


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
            
            final String like = search, orderBy = sortBy;
            CommonResponse result = new CommonResponse();
            Thread t1 = new Thread(new Runnable() {
                public void run() {
                    result.setTableData(
                            service.getResult(like, orderBy, sortDirection, offset, limit, filter, optionalParams));
                }
            });
            t1.start();
            Thread t2 = new Thread(new Runnable() {
                public void run() {
                    result.setTableHeaders(Util.getTableHeaders(queryType));
                    result.setKeys(new ArrayList<>(result.getTableHeaders().keySet()));
                    result.setTotalResults(service.getTotalResultCount(like, filter, optionalParams).getTotalResults());
                }
            });
            t2.start();
            t1.join();
            t2.join();
            String output = gson.toJson(result);
            Cache.insert(queryParams, output);
            return Response.status(200).entity(output).build();
        } catch (HibernateException e) {
            return Response.status(503).entity(e.toString()).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.toString()).build();
        }
    }

}
