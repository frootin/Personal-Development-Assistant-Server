\connect personal_assistant

DROP TRIGGER last_upd_trigger ON tasks;

DROP FUNCTION last_upd_trig();

CREATE FUNCTION last_upd_trig() RETURNS trigger
   LANGUAGE plpgsql AS
$$BEGIN
   IF NEW.status <> OLD.STATUS THEN
      NEW.done_by_utc := current_timestamp;
      NEW.done_by_tmz := NEW.done_by_utc at time zone NEW.task_timezone;
   ELSE
      NEW.done_by_utc := OLD.done_by_utc;
      NEW.done_by_tmz := OLD.done_by_tmz;
   END IF;
   RETURN NEW;
END;$$;

CREATE TRIGGER last_upd_trigger
   BEFORE UPDATE ON tasks
   FOR EACH ROW
   EXECUTE PROCEDURE last_upd_trig();
