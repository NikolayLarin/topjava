let ajaxUrlUsers = "ajax/admin/users/";

// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: ajaxUrlUsers,
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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
                        "asc"
                    ]
                ]
            }),
            updateTable: function () {
                $.get(context.ajaxUrl, updateTableWithData);
            }
        }
    );
});

function setEnable(id, name) {
    let isEnabled = !!($("#checkbox-for-user-" + id).prop("checked"));
    $.ajax({
        type: "POST",
        url: ajaxUrlUsers + "status",
        data: {id: id, enabled: isEnabled}
    }).done(function () {
        updateTable();
        successNoty("User '" + name + "' is " + (isEnabled === true ? "enabled" : "disabled"));
    });
}