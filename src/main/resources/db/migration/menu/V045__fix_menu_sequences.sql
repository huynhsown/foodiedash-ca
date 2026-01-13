SELECT setval(
    pg_get_serial_sequence('menus', 'id'),
    COALESCE((SELECT MAX(id) FROM menus), 0) + 1,
    false
);

SELECT setval(
    pg_get_serial_sequence('menu_items', 'id'),
    COALESCE((SELECT MAX(id) FROM menu_items), 0) + 1,
    false
);

SELECT setval(
    pg_get_serial_sequence('menu_item_options', 'id'),
    COALESCE((SELECT MAX(id) FROM menu_item_options), 0) + 1,
    false
);

SELECT setval(
    pg_get_serial_sequence('menu_item_option_values', 'id'),
    COALESCE((SELECT MAX(id) FROM menu_item_option_values), 0) + 1,
    false
);

