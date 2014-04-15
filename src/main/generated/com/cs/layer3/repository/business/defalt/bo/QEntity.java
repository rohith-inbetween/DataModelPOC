package com.cs.layer3.repository.business.defalt.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QEntity is a Querydsl query type for Entity
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEntity extends EntityPathBase<Entity> {

    private static final long serialVersionUID = -644691297L;

    public static final QEntity entity = new QEntity("entity");

    public final QBusinessObject _super = new QBusinessObject(this);

    public final StringPath EntityName = createString("EntityName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public QEntity(String variable) {
        super(Entity.class, forVariable(variable));
    }

    public QEntity(Path<? extends Entity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEntity(PathMetadata<?> metadata) {
        super(Entity.class, metadata);
    }

}

