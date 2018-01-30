INSERT INTO roles (id, name, description)
VALUES
  (1, 'ROLE_ADMIN', 'admin'),
  (2, 'ROLE_USER', 'user');

INSERT INTO persons (id, first_name, last_name, short_name, email, password, phone, birth_date, gender, created)
VALUES
  (1, 'Gevork', 'Safaryan', 'georgiosafo', 'georgiosafo@gmail.com', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '79164170990', '1991-02-24', 1, '2000-01-01'),
  (2, 'Il', 'Rastorguev', 'rastorguyev', 'Rastorguew94@gmail.com', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1995-03-29', 1, '2000-01-11');

INSERT INTO user_roles (person_id, role_id) VALUES
  (1, 1), (1, 2);

INSERT INTO friends (person_id, friend_id) VALUES
  (2, 1), (1, 2);

INSERT INTO messages (id, posted, sender_id, recipient_id, body) VALUES
  (1, {ts '2016-07-20 00:00:00.0'}, 1, 2, 'Привет'),
  (2, {ts '2016-07-20 00:00:01.0'}, 2, 1, 'Привет'),
  (3, {ts '2016-07-20 12:02:43.0'}, 1, 2, 'Как дела?'),
  (4, {ts '2016-07-20 12:05:34.0'}, 2, 1, 'Нормально!\nА у тебя?'),
  (5, {ts '2016-07-20 12:06:11.0'}, 1, 2, 'Тоже вроде неплохо'),
  (6, {ts '2016-07-20 19:03:19.0'}, 2, 1, 'Сорри...\nМне пора бежать, борщ кипит'),
  (7, {ts '2016-07-20 19:51:11.0'}, 1, 2, 'Проверка\nСвязи'),
  (8, {ts '2016-08-06 16:49:58.0'}, 1, 2, 'Привет, Витамин!')