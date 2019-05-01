package sktest.skava.sql.entity.prepare;

import lombok.ToString;
import lombok.experimental.Accessors;
import org.shaneking.skava.sql.entity.SKEntity;

import javax.persistence.Table;

@Accessors(chain = true)
@ToString(callSuper = true, includeFieldNames = true)
@Table
public class prepareSKEntityTableName extends SKEntity {

}
