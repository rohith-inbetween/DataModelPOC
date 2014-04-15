package com.cs.layer3.repository.business.extension.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QValue is a Querydsl query type for Value
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QValue extends EntityPathBase<Value> {

    private static final long serialVersionUID = -167926844L;

    public static final QValue value1 = new QValue("value1");

    public final com.cs.layer3.repository.business.defalt.bo.QBusinessObject _super = new com.cs.layer3.repository.business.defalt.bo.QBusinessObject(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public final StringPath value = createString("value");

    public QValue(String variable) {
        super(Value.class, forVariable(variable));
    }

    public QValue(Path<? extends Value> path) {
        super(path.getType(), path.getMetadata());
    }

    public QValue(PathMetadata<?> metadata) {
        super(Value.class, metadata);
    }

}

