INSERT INTO roles (id, name)
VALUES (1, 'ADMIN'),
       (2, 'MERCHANT'),
       (3, 'CUSTOMER'),
       (4, 'DRIVER'),
       (5, 'RESTAURANT_DEVICE');

INSERT INTO permissions (id, name, module, description)
VALUES
    (1, 'RESTAURANT_CREATE', 'RESTAURANT', 'Create new restaurant'),
    (2, 'RESTAURANT_UPDATE', 'RESTAURANT', 'Update restaurant info'),
    (3, 'RESTAURANT_APPROVE', 'RESTAURANT', 'Approve restaurant registration'),
    (4, 'RESTAURANT_SUSPEND', 'RESTAURANT', 'Suspend a restaurant'),
    (5, 'RESTAURANT_VIEW', 'RESTAURANT', 'View restaurant details'),
    (6, 'RESTAURANT_DELETE', 'RESTAURANT', 'Soft delete a restaurant'),

    (10, 'MENU_CREATE', 'MENU', 'Create menu'),
    (11, 'MENU_UPDATE', 'MENU', 'Update menu'),
    (12, 'MENU_DELETE', 'MENU', 'Delete menu'),
    (13, 'MENU_ITEM_MANAGE', 'MENU', 'Manage menu items and options'),

    (20, 'ORDER_CREATE', 'ORDER', 'Place an order'),
    (21, 'ORDER_CANCEL', 'ORDER', 'Cancel own order'),
    (22, 'ORDER_VIEW_OWN', 'ORDER', 'View own orders'),
    (23, 'ORDER_VIEW_RESTAURANT', 'ORDER', 'View orders for own restaurant'),
    (24, 'ORDER_VIEW_ALL', 'ORDER', 'View all orders (admin)'),
    (25, 'ORDER_PICKUP', 'ORDER', 'Pick up order (driver)'),
    (26, 'ORDER_DELIVER', 'ORDER', 'Mark order as delivered'),

    (30, 'KITCHEN_VIEW_ORDERS', 'KITCHEN', 'View kitchen order queue'),
    (31, 'KITCHEN_ACCEPT', 'KITCHEN', 'Accept kitchen order'),
    (32, 'KITCHEN_REJECT', 'KITCHEN', 'Reject kitchen order'),
    (33, 'KITCHEN_START_COOKING', 'KITCHEN', 'Start cooking'),
    (34, 'KITCHEN_MARK_READY', 'KITCHEN', 'Mark order ready for pickup'),

    (40, 'CART_MANAGE', 'CART', 'Add/remove/update cart items'),

    (50, 'PROMOTION_CREATE', 'PROMOTION', 'Create promotion'),
    (51, 'PROMOTION_UPDATE', 'PROMOTION', 'Update promotion'),
    (52, 'PROMOTION_DELETE', 'PROMOTION', 'Delete promotion'),
    (53, 'PROMOTION_VIEW', 'PROMOTION', 'View promotions list'),

    (60, 'USER_VIEW', 'USER', 'View user list and details'),
    (61, 'USER_MANAGE', 'USER', 'Suspend/activate/manage users'),

    (70, 'RATING_CREATE', 'RATING', 'Create a rating/review'),

    (80, 'DEVICE_MANAGE', 'DEVICE', 'Create/manage restaurant devices');


INSERT INTO role_permissions (role_id, permission_id)
SELECT 1, id FROM permissions;

INSERT INTO role_permissions (role_id, permission_id)
VALUES (2, 1),  (2, 2),  (2, 5),
       (2, 10), (2, 11), (2, 12), (2, 13),
       (2, 23),
       (2, 30), (2, 31), (2, 32), (2, 33), (2, 34),
       (2, 80);

INSERT INTO role_permissions (role_id, permission_id)
VALUES (3, 5),
       (3, 20), (3, 21), (3, 22),
       (3, 40),
       (3, 70);

INSERT INTO role_permissions (role_id, permission_id)
VALUES (4, 25), (4, 26);

INSERT INTO role_permissions (role_id, permission_id)
VALUES (5, 30), (5, 31), (5, 32), (5, 33), (5, 34);

-- Password: admin123
INSERT INTO users (id, email, phone, password, full_name, status)
VALUES (1, 'admin@foodiedash.com', NULL,
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'System Admin', 'ACTIVE');

INSERT INTO user_roles (user_id, role_name)
VALUES (1, 'ADMIN');