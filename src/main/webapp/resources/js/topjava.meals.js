let ajaxUrlMeals = "ajax/profile/meals/";

// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: ajaxUrlMeals,
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            })
        }
    );
});

function drawFiltered() {
    $.ajax({
        type: "GET",
        url: ajaxUrlMeals + "filter",
        // data: document.getElementById("filterForm").serialize()
        data: $("#filterForm").serialize()
    }).done(function (data) {
        context.datatableApi.clear().rows.add(data).draw();
        successNoty("Filtered")
    });
}

function clearFilters() {
    // document.getElementById("filterForm").reset();
    $("#filterForm")[0].reset();
    updateTable();
    successNoty("Filters are cleaned");
}