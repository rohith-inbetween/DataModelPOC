package com.cs.layer3.repository.business.defalt.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QEntityMapped is a Querydsl query type for EntityMapped
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEntityMapped extends EntityPathBase<EntityMapped> {

    private static final long serialVersionUID = -222117454L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEntityMapped entityMapped = new QEntityMapped("entityMapped");

    public final QBusinessObject _super = new QBusinessObject(this);

    public final MapPath<Attribute, com.cs.layer3.repository.business.extension.bo.Value, com.cs.layer3.repository.business.extension.bo.QValue> attributeValues = this.<Attribute, com.cs.layer3.repository.business.extension.bo.Value, com.cs.layer3.repository.business.extension.bo.QValue>createMap("attributeValues", Attribute.class, com.cs.layer3.repository.business.extension.bo.Value.class, com.cs.layer3.repository.business.extension.bo.QValue.class);

    public final SetPath<ClazzMapped, QClazzMapped> classes = this.<ClazzMapped, QClazzMapped>createSet("classes", ClazzMapped.class, QClazzMapped.class, PathInits.DIRECT2);

    public final StringPath EntityName = createString("EntityName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QEntity parentEntity;

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public QEntityMapped(String variable) {
        this(EntityMapped.class, forVariable(variable), INITS);
    }

    public QEntityMapped(Path<? extends EntityMapped> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEntityMapped(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QEntityMapped(PathMetadata<?> metadata, PathInits inits) {
        this(EntityMapped.class, metadata, inits);
    }

    public QEntityMapped(Class<? extends EntityMapped> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parentEntity = inits.isInitialized("parentEntity") ? new QEntity(forProperty("parentEntity")) : null;
    }

}

