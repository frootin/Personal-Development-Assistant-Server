CREATE DATABASE personal_assistant WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'ru_RU.UTF-8' LC_CTYPE = 'ru_RU.UTF-8';

\connect personal_assistant

CREATE TABLE IF NOT EXISTS public.users
(
    id bigserial PRIMARY KEY,
    email varchar UNIQUE NOT NULL,
    username varchar UNIQUE NOT NULL,
    pass_hash varchar NOT NULL,
    display_name varchar NOT NULL,
    userpic varchar,
    interests varchar,
    created_at timestamp without time zone
);

CREATE TABLE IF NOT EXISTS public.categories
(
    id bigserial PRIMARY KEY,
    title varchar NOT NULL,
    color varchar NOT NULL,
    user_id bigint,
    on_watch boolean NOT NULL,
    created_at timestamp without time zone,
    CONSTRAINT categories_user_id_color_key UNIQUE (user_id, color),
    CONSTRAINT categories_user_id_name_key UNIQUE (user_id, title),
    CONSTRAINT categories_user_id_fkey FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.tasks
(
    id bigserial PRIMARY KEY,
    user_id bigint,
    task_name varchar NOT NULL,
    details varchar,
    estimate integer NOT NULL,
    category_id bigint,
    date_start date,
    date_end date,
    time_start time,
    time_end time,
    task_timezone varchar,
    status integer NOT NULL DEFAULT 0,
    done_by timestamp without time zone,
    CONSTRAINT tasks_category_id_fkey FOREIGN KEY (category_id)
    REFERENCES public.categories (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE,
    CONSTRAINT tasks_user_id_fkey FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.user_plans
(
    id bigserial PRIMARY KEY,
    user_id bigint,
    name varchar NOT NULL,
    details varchar,
    status integer NOT NULL DEFAULT 0,
    full_estimate integer NOT NULL DEFAULT 0,
    categories varchar[],
    CONSTRAINT plans_user_id_fkey FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.tasks_in_plans
(
    task_id bigint,
    plan_id bigint,
    step_number integer NOT NULL,
    CONSTRAINT tasks_in_plans_pkey PRIMARY KEY (task_id, plan_id),
    CONSTRAINT tasks_in_plans_task_id_key UNIQUE (task_id),
    CONSTRAINT tasks_in_plans_plan_id_fkey FOREIGN KEY (plan_id)
    REFERENCES public.user_plans (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE,
    CONSTRAINT tasks_in_plans_task_id_fkey FOREIGN KEY (task_id)
    REFERENCES public.tasks (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);

