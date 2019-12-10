let mealAjaxUrl1 = "ajax/profile/meals/";

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: "ajax/profile/meals/filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get("ajax/profile/meals/", updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: mealAjaxUrl1,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl1,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        // if (type === "display") {
                        return date.substring(0, 16).replace("T", " ");
                        // }
                        // return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "data": "excess",
                    "visible": false,
                    "orderable": false
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess);
            }
        }),
        updateTable: updateFilteredTable
    });
});