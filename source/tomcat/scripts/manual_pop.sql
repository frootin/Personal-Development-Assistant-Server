\connect personal_assistant

INSERT INTO public.users(email, username, pass_hash, display_name, userpic, interests, created_at)
    VALUES ('user1@gmail.com', 'user1', 'df34hvddg678ccvv', 'Василиса', 'link', 'интересно', NOW()::timestamp),
           ('user2@gmail.com', 'user2', 'df34hvddg678ccvv', 'Skel', 'link', 'интересно', NOW()::timestamp);

INSERT INTO public.categories(title, color, user_id, on_watch, created_at)
    VALUES ('Здоровье', '#03fc4e', 1, true, NOW()::timestamp),
           ('Семья', '#a103fc', 1, true, NOW()::timestamp),
           ('Учёба', '#fc8c03', 1, true, NOW()::timestamp),
           ('Спорт', '#0366fc', 1, true, NOW()::timestamp),
           ('Здоровье', '#03fc4e', 2, true, NOW()::timestamp),
           ('Семья', '#a103fc', 2, true, NOW()::timestamp),
           ('Учёба', '#fc8c03', 2, true, NOW()::timestamp),
           ('Спорт', '#0366fc', 2, true, NOW()::timestamp);

INSERT INTO public.user_settings(user_id, events_track_start_date, events_track_weeks_num, week_start_day)
    VALUES (1, '2023-12-12'::date, 2, 'Monday');

INSERT INTO public.user_plans(user_id, plan_name, details, status)
    VALUES (1, 'Поступление', 'Очень надо', 0),
           (1, 'Проект', 'Для себя', 1);

INSERT INTO public.repeats(user_id, task_name, details, estimate, category_id, date_start, date_end, time_start, time_end, task_timezone, repeat_term, repeat_days, repeat_start,repeat_end, plan_id)
    VALUES (1, 'Подать документы', 'В университет', 3, 3, null, null, null, null, '', 'week', '{1, 3, 5}', '2023-12-13'::date, '2024-12-13'::date, null);

INSERT INTO public.tasks(user_id, task_name, details, estimate, category_id, date_start, date_end, time_start, time_end, task_timezone, status, done_by, repeat_id)
    VALUES (1, 'Подать документы', 'В университет', 3, 3, null, null, null, null, '', 0, null, 1),
           (1, 'Силовые', 'Тяжело', 5, 4, '2023-12-12'::date, '2023-12-12'::date, '10:15'::time, '11:15'::time, '', 0, null, null),
           (1, 'Купить подарки', 'Зайти в книжный', 4, 2, '2023-12-13'::date, '2023-12-13'::date, '12:15'::time, '17:15'::time, '', 0, null, null),
           (1, 'Доделать проект', 'Книга', 6, 3, '2023-12-12'::date, '2023-12-12'::date, null, null, '', 0, null, null),
           (1, 'Силовые', 'Тяжело', 5, 4, '2023-12-13'::date, '2023-12-13'::date, '10:15'::time, '10:15'::time, '', 0, null, null),
           (1, 'Пробежка', 'Сила-спорт', 5, 4, '2023-12-11'::date, '2023-12-11'::date, '10:15'::time, '10:15'::time, '', 0, null, null);

INSERT INTO public.tasks_in_plans(task_id, plan_id, step_number)
    VALUES (1, 1, 1);

INSERT INTO public.user_events(user_id, week_num, day_of_week, event_name, place, event_format, start_time, stop_time)
    VALUES (1, 1, 2, 'Машинно-зависимые языки программирования', 'ссылка', 'лекция', '10:15'::time, '11:50'::time),
           (1, 1, 2, 'Командный проект', 'ссылка', 'практика', '15:55'::time, '17:30'::time),
           (1, 1, 4, 'Машинно-зависимые языки программирования', '4-16', 'практика', '14:10'::time, '15:45'::time),
           (1, 2, 4, 'Машинно-зависимые языки программирования', '4-16', 'практика', '14:10'::time, '15:45'::time);

INSERT INTO public.day_notes(user_id, assigned_day, note_text)
    VALUES (1, '2023-12-12'::date, 'Заметка 1'),
           (1, '2023-12-13'::date, 'Заметка 2');

INSERT INTO public.diary_entries(user_id, entry_text, assigned_day, updated_at)
    VALUES (1, 'Запись 1', '2023-12-12'::date, NOW()::timestamp),
           (1, 'Запись 2', '2023-12-13'::date, NOW()::timestamp);
